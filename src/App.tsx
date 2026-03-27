/**
 * @license
 * SPDX-License-Identifier: Apache-2.0
 */

import React, { useState, useEffect } from 'react';
import { 
  Timer, 
  History, 
  BarChart3, 
  Settings, 
  Lock, 
  Play, 
  Pause, 
  X, 
  Flame, 
  Trophy,
  ChevronLeft,
  Volume2,
  Vibrate
} from 'lucide-react';
import { motion, AnimatePresence } from 'motion/react';

// --- Types ---
type Screen = 'dashboard' | 'timer' | 'history' | 'stats' | 'settings' | 'blocking';

interface Session {
  id: number;
  date: string;
  duration: number;
  type: 'Focus' | 'Short Break' | 'Long Break';
}

// --- Mock Data ---
const MOCK_HISTORY: Session[] = [
  { id: 1, date: 'Oct 24, 2023 14:30', duration: 25, type: 'Focus' },
  { id: 2, date: 'Oct 24, 2023 15:00', duration: 5, type: 'Short Break' },
  { id: 3, date: 'Oct 24, 2023 15:30', duration: 25, type: 'Focus' },
];

const QUOTES = [
  "Focus on being productive instead of busy.",
  "Your focus determines your reality.",
  "Starve your distractions, feed your focus.",
  "Deep work is the superpower of the 21st century.",
];

export default function App() {
  const [currentScreen, setCurrentScreen] = useState<Screen>('dashboard');
  const [isTimerRunning, setIsTimerRunning] = useState(false);
  const [timeLeft, setTimeLeft] = useState(25 * 60);
  const [timerType, setTimerType] = useState<'Focus' | 'Break'>('Focus');
  const [streak, setStreak] = useState(5);
  const [sessionsCount, setSessionsCount] = useState(42);

  // Timer Logic
  useEffect(() => {
    let interval: any;
    if (isTimerRunning && timeLeft > 0) {
      interval = setInterval(() => {
        setTimeLeft((prev) => prev - 1);
      }, 1000);
    } else if (timeLeft === 0) {
      setIsTimerRunning(false);
      // Mock session completion
      if (timerType === 'Focus') {
        setSessionsCount(s => s + 1);
      }
    }
    return () => clearInterval(interval);
  }, [isTimerRunning, timeLeft, timerType]);

  const formatTime = (seconds: number) => {
    const m = Math.floor(seconds / 60);
    const s = seconds % 60;
    return `${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}`;
  };

  const progress = (timeLeft / (25 * 60)) * 100;

  // --- Components ---

  const Header = ({ title, showBack = true }: { title: string, showBack?: boolean }) => (
    <div className="flex items-center justify-between mb-6">
      <div className="flex items-center gap-3">
        {showBack && (
          <button 
            onClick={() => setCurrentScreen('dashboard')}
            className="p-2 rounded-full bg-[#1E1E1E] text-[#BB86FC]"
          >
            <ChevronLeft size={24} />
          </button>
        )}
        <h1 className="text-2xl font-black text-white">{title}</h1>
      </div>
    </div>
  );

  const Dashboard = () => (
    <div className="p-6 pb-24">
      <div className="mb-8">
        <p className="text-[#B0B0B0] text-sm">Welcome Back,</p>
        <h2 className="text-3xl font-black text-white">Focus Warrior</h2>
      </div>

      {/* Quote Card */}
      <motion.div 
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="bg-[#1E1E1E] border border-[#BB86FC]/30 rounded-2xl p-6 mb-8 text-center"
      >
        <p className="text-white italic mb-6">"{QUOTES[0]}"</p>
        <button 
          onClick={() => setCurrentScreen('timer')}
          className="bg-[#BB86FC] text-black font-bold py-3 px-8 rounded-xl hover:scale-105 transition-transform"
        >
          Start Focus
        </button>
      </motion.div>

      {/* Stats Grid */}
      <div className="grid grid-cols-2 gap-4 mb-8">
        {[
          { label: 'Today', value: '2h 15m', color: 'text-[#03DAC6]', icon: <Timer size={16} /> },
          { label: 'Sessions', value: sessionsCount, color: 'text-[#BB86FC]', icon: <Trophy size={16} /> },
          { label: 'Streak', value: `${streak} Days`, color: 'text-[#FF4081]', icon: <Flame size={16} /> },
          { label: 'Rank', value: 'Expert', color: 'text-[#3700B3]', icon: <BarChart3 size={16} /> },
        ].map((stat, i) => (
          <motion.div 
            key={i}
            initial={{ opacity: 0, scale: 0.9 }}
            animate={{ opacity: 1, scale: 1 }}
            transition={{ delay: i * 0.1 }}
            className="bg-[#1E1E1E] rounded-2xl p-4"
          >
            <div className="flex items-center gap-2 text-[#B0B0B0] text-xs mb-1">
              {stat.icon}
              {stat.label}
            </div>
            <div className={`text-xl font-bold ${stat.color}`}>{stat.value}</div>
          </motion.div>
        ))}
      </div>

      <h3 className="text-white font-bold text-lg mb-4">Quick Actions</h3>
      <div className="grid grid-cols-4 gap-3">
        {[
          { id: 'blocking', icon: <Lock size={24} />, label: 'Block', color: 'text-[#03DAC6]' },
          { id: 'history', icon: <History size={24} />, label: 'History', color: 'text-[#BB86FC]' },
          { id: 'stats', icon: <BarChart3 size={24} />, label: 'Stats', color: 'text-[#FF4081]' },
          { id: 'settings', icon: <Settings size={24} />, label: 'Settings', color: 'text-[#3700B3]' },
        ].map((action) => (
          <button 
            key={action.id}
            onClick={() => setCurrentScreen(action.id as Screen)}
            className="bg-[#1E1E1E] rounded-2xl p-4 flex flex-col items-center gap-2"
          >
            <div className={action.color}>{action.icon}</div>
            <span className="text-white text-[10px] uppercase tracking-wider">{action.label}</span>
          </button>
        ))}
      </div>
    </div>
  );

  const TimerScreen = () => (
    <div className="p-8 h-full flex flex-col items-center justify-between">
      <Header title="Focus Timer" />
      
      <div className="flex flex-col items-center gap-4">
        <h2 className="text-[#BB86FC] text-2xl font-black uppercase tracking-widest">{timerType} Session</h2>
        
        <div className="relative w-72 h-72 flex items-center justify-center">
          <svg className="w-full h-full -rotate-90">
            <circle
              cx="144"
              cy="144"
              r="130"
              fill="transparent"
              stroke="#333333"
              strokeWidth="12"
            />
            <motion.circle
              cx="144"
              cy="144"
              r="130"
              fill="transparent"
              stroke="#BB86FC"
              strokeWidth="12"
              strokeDasharray="816"
              initial={{ strokeDashoffset: 816 }}
              animate={{ strokeDashoffset: 816 - (816 * progress) / 100 }}
              strokeLinecap="round"
            />
          </svg>
          <div className="absolute text-6xl font-mono font-bold text-white">
            {formatTime(timeLeft)}
          </div>
        </div>
      </div>

      <div className="flex items-center gap-8 mb-12">
        <button 
          onClick={() => {
            setIsTimerRunning(false);
            setCurrentScreen('dashboard');
          }}
          className="w-14 h-14 rounded-full bg-[#1E1E1E] flex items-center justify-center text-[#FF4081]"
        >
          <X size={28} />
        </button>
        
        <button 
          onClick={() => setIsTimerRunning(!isTimerRunning)}
          className="bg-[#BB86FC] text-black px-10 py-4 rounded-3xl flex items-center gap-3 font-black text-xl"
        >
          {isTimerRunning ? <Pause fill="black" /> : <Play fill="black" />}
          {isTimerRunning ? 'PAUSE' : 'START'}
        </button>
      </div>
    </div>
  );

  const HistoryScreen = () => (
    <div className="p-6">
      <Header title="Focus History" />
      <div className="space-y-4">
        {MOCK_HISTORY.map((session) => (
          <div key={session.id} className="bg-[#1E1E1E] rounded-2xl p-4 flex justify-between items-center">
            <div>
              <div className="text-[#BB86FC] font-bold">{session.type} Session</div>
              <div className="text-[#B0B0B0] text-xs">{session.date}</div>
            </div>
            <div className="text-white font-bold">{session.duration} min</div>
          </div>
        ))}
      </div>
    </div>
  );

  const StatsScreen = () => (
    <div className="p-6">
      <Header title="Statistics" />
      <div className="bg-[#1E1E1E] rounded-2xl p-6 mb-6 h-64 flex items-end justify-between gap-2">
        {[40, 60, 30, 80, 70, 90, 65].map((h, i) => (
          <div key={i} className="flex-1 flex flex-col items-center gap-2">
            <motion.div 
              initial={{ height: 0 }}
              animate={{ height: `${h}%` }}
              className="w-full bg-[#BB86FC] rounded-t-lg"
            />
            <span className="text-[10px] text-[#B0B0B0]">{"MTWTFSS"[i]}</span>
          </div>
        ))}
      </div>
      <div className="grid grid-cols-2 gap-4">
        <div className="bg-[#1E1E1E] rounded-2xl p-4">
          <div className="text-[#B0B0B0] text-xs">Total Focus</div>
          <div className="text-[#03DAC6] text-xl font-bold">12h 45m</div>
        </div>
        <div className="bg-[#1E1E1E] rounded-2xl p-4">
          <div className="text-[#B0B0B0] text-xs">Total Sessions</div>
          <div className="text-[#BB86FC] text-xl font-bold">42</div>
        </div>
      </div>
    </div>
  );

  const SettingsScreen = () => (
    <div className="p-6">
      <Header title="Settings" />
      <div className="bg-[#1E1E1E] rounded-2xl overflow-hidden mb-8">
        {[
          { label: 'Dark Mode', icon: <div className="w-2 h-2 rounded-full bg-white" />, active: true },
          { label: 'Sound Effects', icon: <Volume2 size={20} />, active: false },
          { label: 'Vibration', icon: <Vibrate size={20} />, active: true },
        ].map((item, i) => (
          <div key={i} className="flex items-center justify-between p-4 border-b border-[#333333] last:border-0">
            <div className="flex items-center gap-3 text-white">
              <div className="text-[#BB86FC]">{item.icon}</div>
              {item.label}
            </div>
            <div className={`w-12 h-6 rounded-full p-1 transition-colors ${item.active ? 'bg-[#BB86FC]' : 'bg-[#333333]'}`}>
              <div className={`w-4 h-4 rounded-full bg-white transition-transform ${item.active ? 'translate-x-6' : 'translate-x-0'}`} />
            </div>
          </div>
        ))}
      </div>
      
      <h4 className="text-[#B0B0B0] text-sm mb-4">Timer Durations (min)</h4>
      <div className="bg-[#1E1E1E] rounded-2xl p-6 space-y-6">
        <div>
          <label className="text-[#BB86FC] text-xs block mb-2">Focus Duration</label>
          <input type="number" defaultValue={25} className="bg-transparent border-b border-[#333333] w-full text-white pb-2 outline-none focus:border-[#BB86FC]" />
        </div>
        <div>
          <label className="text-[#03DAC6] text-xs block mb-2">Short Break</label>
          <input type="number" defaultValue={5} className="bg-transparent border-b border-[#333333] w-full text-white pb-2 outline-none focus:border-[#03DAC6]" />
        </div>
      </div>
    </div>
  );

  const BlockingScreen = () => (
    <div className="p-6">
      <Header title="App Blocking" />
      <p className="text-[#B0B0B0] text-sm mb-6">Select apps to block during focus sessions</p>
      <div className="space-y-4 mb-8">
        {['Instagram', 'YouTube', 'WhatsApp', 'Twitter', 'Facebook'].map((app, i) => (
          <div key={i} className="bg-[#1E1E1E] rounded-2xl p-4 flex items-center justify-between">
            <div className="flex items-center gap-4">
              <div className="w-10 h-10 bg-[#333333] rounded-xl flex items-center justify-center text-white font-bold">
                {app[0]}
              </div>
              <span className="text-white font-medium">{app}</span>
            </div>
            <div className="w-12 h-6 rounded-full p-1 bg-[#333333]">
              <div className="w-4 h-4 rounded-full bg-white" />
            </div>
          </div>
        ))}
      </div>
      <button 
        onClick={() => setCurrentScreen('dashboard')}
        className="w-full bg-[#BB86FC] text-black font-bold py-4 rounded-2xl"
      >
        Save Configuration
      </button>
    </div>
  );

  return (
    <div className="min-h-screen bg-[#121212] font-sans text-white overflow-x-hidden">
      <div className="max-w-md mx-auto min-h-screen relative shadow-2xl shadow-black/50">
        
        <AnimatePresence mode="wait">
          <motion.div
            key={currentScreen}
            initial={{ opacity: 0, x: 20 }}
            animate={{ opacity: 1, x: 0 }}
            exit={{ opacity: 0, x: -20 }}
            transition={{ duration: 0.2 }}
            className="h-full"
          >
            {currentScreen === 'dashboard' && <Dashboard />}
            {currentScreen === 'timer' && <TimerScreen />}
            {currentScreen === 'history' && <HistoryScreen />}
            {currentScreen === 'stats' && <StatsScreen />}
            {currentScreen === 'settings' && <SettingsScreen />}
            {currentScreen === 'blocking' && <BlockingScreen />}
          </motion.div>
        </AnimatePresence>

        {/* Bottom Navigation Bar (Android Style) */}
        {currentScreen !== 'timer' && (
          <div className="fixed bottom-0 left-0 right-0 max-w-md mx-auto bg-[#1E1E1E] border-t border-[#333333] px-6 py-4 flex justify-between items-center z-50">
            <button onClick={() => setCurrentScreen('dashboard')} className={currentScreen === 'dashboard' ? 'text-[#BB86FC]' : 'text-[#B0B0B0]'}>
              <Timer size={24} />
            </button>
            <button onClick={() => setCurrentScreen('history')} className={currentScreen === 'history' ? 'text-[#BB86FC]' : 'text-[#B0B0B0]'}>
              <History size={24} />
            </button>
            <button onClick={() => setCurrentScreen('stats')} className={currentScreen === 'stats' ? 'text-[#BB86FC]' : 'text-[#B0B0B0]'}>
              <BarChart3 size={24} />
            </button>
            <button onClick={() => setCurrentScreen('settings')} className={currentScreen === 'settings' ? 'text-[#BB86FC]' : 'text-[#B0B0B0]'}>
              <Settings size={24} />
            </button>
          </div>
        )}
      </div>
    </div>
  );
}
