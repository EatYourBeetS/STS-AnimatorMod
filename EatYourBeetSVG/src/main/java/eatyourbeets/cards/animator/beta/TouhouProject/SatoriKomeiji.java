package eatyourbeets.cards.animator.beta.TouhouProject;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.monsters.exordium.Hexaghost;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.ui.PreviewIntent;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;

import java.util.ArrayList;
import java.util.Iterator;

public class SatoriKomeiji extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SatoriKomeiji.class).SetPower(0, CardRarity.RARE);

    public SatoriKomeiji()
    {
        super(DATA);

        Initialize(0, 0, 0, 0);
        SetUpgrade(0, 0, 0, 0);
        SetScaling(0, 0, 0);

        SetInnate(true);
        SetSynergy(Synergies.TouhouProject);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.ApplyPower(new SatoriPower(p));
    }

    public static class SatoriPower extends AnimatorPower
    {
        ArrayList<PreviewIntent> previewIntents = new ArrayList<>();

        public static int StabbyMcStabs = 1;
        public static boolean entangleReset = false;

        public static boolean champThresholdReached;
        public static int champNumTurns;
        public static int forgeTimes;

        public SatoriPower(AbstractCreature owner)
        {
            super(owner, SatoriKomeiji.DATA);
            updateDescription();
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();
            GameActions.Bottom.Callback(this::updatePreviews);
        }

        @Override
        public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
        {
            super.onApplyPower(power, target, source);
            if (!(source instanceof AbstractMonster)) {
                GameActions.Bottom.Callback(this::updatePreviews);
            }
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();
            GameActions.Bottom.Callback(this::updatePreviews);
        }

        private void updatePreviews()
        {
            previewIntents.clear();
            for (AbstractMonster mo : GameUtilities.GetEnemies(true))
            {
                FieldInfo<EnemyMoveInfo> _move = JavaUtilities.GetField("move", AbstractMonster.class);
                EnemyMoveInfo originalMove = _move.Get(mo);
                int counter = AbstractDungeon.aiRng.counter;
                long seed0 = (Long) ReflectionHacks.getPrivate(AbstractDungeon.aiRng.random, com.badlogic.gdx.math.RandomXS128.class, "seed0");
                long seed1 = (Long) ReflectionHacks.getPrivate(AbstractDungeon.aiRng.random, com.badlogic.gdx.math.RandomXS128.class, "seed1");

                previewNextIntent(mo);
                mo.rollMove();
                EnemyMoveInfo nextMove = _move.Get(mo);

                //removes the move add by the above rollMove from move history
                mo.moveHistory.remove(mo.moveHistory.size() - 1);
                restorePreviewedSpecialCases(mo);
                mo.setMove(originalMove.nextMove, originalMove.intent, originalMove.baseDamage, originalMove.multiplier, originalMove.isMultiDamage);
                //removes the move added by the above setMove from move history
                mo.moveHistory.remove(mo.moveHistory.size() - 1);

                AbstractDungeon.aiRng.counter = counter;
                AbstractDungeon.aiRng.random.setState(seed0, seed1);

                PreviewIntent previewIntent = applyPreviewPowers(mo, nextMove);
                previewIntents.add(previewIntent);
            }
        }

        private PreviewIntent applyPreviewPowers(AbstractMonster source, EnemyMoveInfo move)
        {
            PreviewIntent previewIntent = new PreviewIntent(source, move);
            if (move.baseDamage > -1)
            {
                AbstractPlayer target = AbstractDungeon.player;
                int dmg = move.baseDamage;
                float tmp = (float) dmg;
                if (Settings.isEndless && AbstractDungeon.player.hasBlight("DeadlyEnemies"))
                {
                    float mod = AbstractDungeon.player.getBlight("DeadlyEnemies").effectFloat();
                    tmp *= mod;
                }

                AbstractPower p;
                Iterator var6;
                for (var6 = source.powers.iterator(); var6.hasNext(); tmp = p.atDamageGive(tmp, DamageInfo.DamageType.NORMAL))
                {
                    p = (AbstractPower) var6.next();
                }

                for (var6 = target.powers.iterator(); var6.hasNext(); tmp = p.atDamageReceive(tmp, DamageInfo.DamageType.NORMAL))
                {
                    p = (AbstractPower) var6.next();
                }

                tmp = AbstractDungeon.player.stance.atDamageReceive(tmp, DamageInfo.DamageType.NORMAL);

                for (var6 = source.powers.iterator(); var6.hasNext(); tmp = p.atDamageFinalGive(tmp, DamageInfo.DamageType.NORMAL))
                {
                    p = (AbstractPower) var6.next();
                }

                for (var6 = target.powers.iterator(); var6.hasNext(); tmp = p.atDamageFinalReceive(tmp, DamageInfo.DamageType.NORMAL))
                {
                    p = (AbstractPower) var6.next();
                }

                dmg = MathUtils.floor(tmp);
                if (dmg < 0)
                {
                    dmg = 0;
                }

                previewIntent.updateDamage(dmg);
            }
            return previewIntent;
        }

        @Override
        public void renderIcons(SpriteBatch sb, float x, float y, Color c)
        {
            super.renderIcons(sb, x, y, c);

            for (PreviewIntent intent : previewIntents)
            {
                intent.update();
                intent.render(sb);
            }
        }

        @Override
        public void updateDescription()
        {
            this.description = powerStrings.DESCRIPTIONS[0];
        }

        public static boolean previewNextIntent(AbstractMonster m)
        {
            int count;
            int turnCount;
            int slashCount;
            boolean isAttacking;

            // Special Cases
            switch (m.id)
            {
                case "AwakenedOne":
                    ReflectionHacks.setPrivate(m, m.getClass(), "firstTurn", false);
                    break;

                // Stupid champ and his junk
                case "Champ":
                    champThresholdReached = (boolean) ReflectionHacks.getPrivate(m, m.getClass(), "thresholdReached");
                    champNumTurns = (int) ReflectionHacks.getPrivate(m, m.getClass(), "numTurns");
                    forgeTimes = (int) ReflectionHacks.getPrivate(m, m.getClass(), "forgeTimes");
                    break;

                // Donu and deca need to swap
                case "Deca":
                    isAttacking = (boolean) ReflectionHacks.getPrivate(m, m.getClass(), "isAttacking");
                    ReflectionHacks.setPrivate(m, m.getClass(), "isAttacking", !isAttacking);
                    break;
                case "Donu":
                    isAttacking = (boolean) ReflectionHacks.getPrivate(m, m.getClass(), "isAttacking");
                    ReflectionHacks.setPrivate(m, m.getClass(), "isAttacking", !isAttacking);
                    break;

                case "TimeEater":
                    if (m.currentHealth < m.maxHealth / 2)
                    {
                        ReflectionHacks.setPrivate(m, m.getClass(), "usedHaste", true);
                    }
                    break;

                // Transient's count increases
                case "Transient":
                    count = (int) ReflectionHacks.getPrivate(m, m.getClass(), "count");
                    ReflectionHacks.setPrivate(m, m.getClass(), "count", count + 1);
                    break;

                // There's no avoiding Hyper Beam charging... but you can avoid Hyper Beam....?!
                case "BronzeAutomaton":
                    count = (int) ReflectionHacks.getPrivate(m, m.getClass(), "numTurns");
                    if (count >= 4)
                    {
                        count = 0;
                    }
                    ReflectionHacks.setPrivate(m, m.getClass(), "numTurns", count);
                    break;

                // Looter is annoying, just like Mugger
                case "Looter":
                case "Mugger":
                    switch (m.nextMove)
                    {
                        case 1:
                            slashCount = (int) ReflectionHacks.getPrivate(m, m.getClass(), "slashCount");
                            m.rollMove();

                            if (slashCount == 1)
                            {
                                m.setMove((byte) 4, AbstractMonster.Intent.ATTACK, ((DamageInfo) m.damage.get(1)).base);
                            }
                            break;
                        case 4:
                            m.setMove((byte) 2, AbstractMonster.Intent.DEFEND);
                            break;
                        case 2:
                            m.setMove((byte) 3, AbstractMonster.Intent.ESCAPE);
                            break;
                        case 3:
                            m.tint.changeColor(Color.CLEAR.cpy(), 0.6F);
                            m.setMove((byte) 0, AbstractMonster.Intent.NONE);
                            break;
                    }
                    return true;

                // Book of Stabbing has some new stuff after A18 that we need to save and restore.
                case "BookOfStabbing":
                    StabbyMcStabs = (int) ReflectionHacks.getPrivate(m, m.getClass(), "stabCount");
                    break;

                // Bandits are all dumb and hardcoded now.
                case "BanditBear":
                    switch (m.nextMove)
                    {
                        case 2:
                            m.setMove((byte) 3, AbstractMonster.Intent.ATTACK_DEFEND, ((DamageInfo) m.damage.get(1)).base);
                            break;
                        case 1:
                            m.setMove((byte) 3, AbstractMonster.Intent.ATTACK_DEFEND, ((DamageInfo) m.damage.get(1)).base);
                            break;
                        case 3:
                            m.setMove((byte) 1, AbstractMonster.Intent.ATTACK_DEFEND, ((DamageInfo) m.damage.get(0)).base);
                            break;
                    }
                    return true;

                case "BanditLeader":
                    switch (m.nextMove)
                    {
                        case 2:
                            m.setMove((byte) 3, AbstractMonster.Intent.ATTACK_DEBUFF, ((DamageInfo) m.damage.get(1)).base);
                            break;
                        case 1:
                            m.setMove((byte) 1, AbstractMonster.Intent.ATTACK, ((DamageInfo) m.damage.get(0)).base);
                            break;
                        case 3:
                            if (AbstractDungeon.ascensionLevel >= 17)
                            {
                                m.setMove((byte) 1, AbstractMonster.Intent.ATTACK_DEFEND, ((DamageInfo) m.damage.get(0)).base);
                            }
                            else
                            {
                                m.setMove((byte) 3, AbstractMonster.Intent.ATTACK_DEBUFF, ((DamageInfo) m.damage.get(1)).base);
                            }
                            break;
                    }
                    return true;

                // The Collector has all sorts of stuff, but you can skip his ult
                case "TheCollector":
                    int turnsTaken = (int) ReflectionHacks.getPrivate(m, m.getClass(), "turnsTaken") + 1;
                    ReflectionHacks.setPrivate(m, m.getClass(), "turnsTaken", turnsTaken);
                    ReflectionHacks.setPrivate(m, m.getClass(), "initialSpawn", false);
                    if (m.intent == AbstractMonster.Intent.STRONG_DEBUFF)
                    {
                        ReflectionHacks.setPrivate(m, m.getClass(), "ultUsed", true);
                    }
                    break;

                // Writhing Mass test code?
                case "WrithingMass":
                    if (m.intent == AbstractMonster.Intent.STRONG_DEBUFF)
                    {
                        ReflectionHacks.setPrivate(m, m.getClass(), "usedMegaDebuff", true);
                    }
                    break;

                // Slimes get split triggered reset, so they will do another intent until you hit them again
                case "AcidSlime_L":
                    ReflectionHacks.setPrivate(m, m.getClass(), "splitTriggered", false);
                    break;

                // Gremlin Wizard charges more or goes back to no charge
                case "GremlinWizard":
                    int currentCharge = (int) ReflectionHacks.getPrivate(m, m.getClass(), "currentCharge");

                    if (currentCharge >= 3)
                    {
                        m.setMove((byte) 1, AbstractMonster.Intent.ATTACK, ((DamageInfo) m.damage.get(0)).base);
                    }

                    else if ((m.intent == AbstractMonster.Intent.ATTACK))
                    {
                        if (AbstractDungeon.ascensionLevel >= 17)
                        {
                            m.setMove((byte) 1, AbstractMonster.Intent.ATTACK, ((DamageInfo) m.damage.get(0)).base);
                        }
                        else
                        {
                            m.setMove((byte) 2, AbstractMonster.Intent.UNKNOWN);
                        }
                    }
                    else
                    {
                        m.setMove((byte) 2, AbstractMonster.Intent.UNKNOWN);
                    }
                    return true;

                // Hexaghost, ouch
                case "Hexaghost":
                    Hexaghost hexa = (Hexaghost) m;

                    // If you skip the first turn, you need to activate him anyway
                    if (m.intent == AbstractMonster.Intent.UNKNOWN)
                    {
                        int d = AbstractDungeon.player.currentHealth / 12 + 1;

                        ((DamageInfo) m.damage.get(2)).base = d;

                        m.applyPowers();
                        m.setMove((byte) 1, AbstractMonster.Intent.ATTACK, d, 6, true);
                        return true;
                    }
                    else
                    {
                        int orbActiveCount = (int) ReflectionHacks.getPrivate(hexa, Hexaghost.class, "orbActiveCount");
                        if (orbActiveCount == 6)
                        {
                            ReflectionHacks.setPrivate(hexa, Hexaghost.class, "orbActiveCount", 0);
                        }
                        else
                        {
                            ReflectionHacks.setPrivate(hexa, Hexaghost.class, "orbActiveCount", orbActiveCount + 1);
                        }
                    }
                    break;

                // Lagavulin wakes up faster, but is otherwise the same
                case "Lagavulin":
                    switch (m.nextMove)
                    {
                        case 5: // sleep
                            int idleCount = (int) ReflectionHacks.getPrivate(m, m.getClass(), "idleCount");
                            ReflectionHacks.setPrivate(m, m.getClass(), "idleCount", idleCount + 1);
                            if (idleCount + 1 >= 3)
                            {
                                m.setMove((byte) 3, AbstractMonster.Intent.ATTACK, ((DamageInfo) m.damage.get(0)).base);
                                return true;
                            }
                            break;
                        case 1:
                            ReflectionHacks.setPrivate(m, m.getClass(), "debuffTurnCount", 0);
                            break;
                        case 3:
                            int debuffTurnCount = (int) ReflectionHacks.getPrivate(m, m.getClass(), "debuffTurnCount");
                            ReflectionHacks.setPrivate(m, m.getClass(), "debuffTurnCount", debuffTurnCount + 1);
                            break;
                    }
                    break;

                // Slaver should only try to use entangle once
                case "SlaverRed":
                    ReflectionHacks.setPrivate(m, m.getClass(), "firstMove", false);
                    if (m.nextMove == 2)
                    {
                        ReflectionHacks.setPrivate(m, m.getClass(), "usedEntangle", true);
                        entangleReset = true;
                    }
                    break;

                // Slimes get split triggered reset, so they will do another intent until you hit them again
                case "SlimeBoss":
                    ReflectionHacks.setPrivate(m, m.getClass(), "firstTurn", false);
                    switch (m.nextMove)
                    {
                        case 4:
                            m.setMove((byte) 2, AbstractMonster.Intent.UNKNOWN);
                            break;
                        case 2:
                            m.setMove((byte) 1, AbstractMonster.Intent.ATTACK, ((DamageInfo) m.damage.get(1)).base);
                            break;
                        case 1:
                            m.setMove((byte) 4, AbstractMonster.Intent.STRONG_DEBUFF);
                            break;
                        case 3:
                            m.setMove((byte) 4, AbstractMonster.Intent.STRONG_DEBUFF);
                            break;
                    }
                    break;

                // Slimes get split triggered reset, so they will do another intent until you hit them again
                case "SpikeSlime_L":
                    ReflectionHacks.setPrivate(m, m.getClass(), "splitTriggered", false);
                    break;

                case "Maw":
                    ReflectionHacks.setPrivate(m, m.getClass(), "roared", true);
                    break;

                // Special case guardian stuff
                case "TheGuardian":
                    switch (m.nextMove)
                    {
                        case 1:
                            m.setMove((byte) 3, AbstractMonster.Intent.ATTACK, ((DamageInfo) m.damage.get(1)).base);
                            break;
                        case 2:
                            m.setMove((byte) 7, AbstractMonster.Intent.STRONG_DEBUFF);
                            break;
                        case 3:
                            m.setMove((byte) 4, AbstractMonster.Intent.ATTACK_BUFF, (int) ReflectionHacks.getPrivate(m, m.getClass(), "twinSlamDamage"), 2, true);
                            break;
                        case 4:
                            m.setMove((byte) 5, AbstractMonster.Intent.ATTACK, (int) ReflectionHacks.getPrivate(m, m.getClass(), "whirlwindDamage"), (int) ReflectionHacks.getPrivate(m, m.getClass(), "whirlwindCount"), true);
                            break;
                        case 5:
                            m.setMove((byte) 6, AbstractMonster.Intent.DEFEND);
                            break;
                        case 6:
                            m.setMove((byte) 2, AbstractMonster.Intent.ATTACK, ((DamageInfo) m.damage.get(0)).base);
                            break;
                        case 7:
                            m.setMove((byte) 5, AbstractMonster.Intent.ATTACK, (int) ReflectionHacks.getPrivate(m, m.getClass(), "whirlwindDamage"), (int) ReflectionHacks.getPrivate(m, m.getClass(), "whirlwindCount"), true);
                            break;
                    }
                    return true;
            }
            return true;
        }

        public static boolean restorePreviewedSpecialCases(AbstractMonster m)
        {
            int count;
            int turnCount;
            int slashCount;
            boolean isAttacking;

            switch (m.id)
            {
                // Donu and deca need to swap
                case "Deca":
                    isAttacking = (boolean) ReflectionHacks.getPrivate(m, m.getClass(), "isAttacking");
                    ReflectionHacks.setPrivate(m, m.getClass(), "isAttacking", !isAttacking);
                    break;
                case "Donu":
                    isAttacking = (boolean) ReflectionHacks.getPrivate(m, m.getClass(), "isAttacking");
                    ReflectionHacks.setPrivate(m, m.getClass(), "isAttacking", !isAttacking);
                    break;

                case "GiantHead":
                    count = (int) ReflectionHacks.getPrivate(m, m.getClass(), "count");
                    ReflectionHacks.setPrivate(m, m.getClass(), "count", count + 1);
                    break;

                case "Transient":
                    count = (int) ReflectionHacks.getPrivate(m, m.getClass(), "count");
                    ReflectionHacks.setPrivate(m, m.getClass(), "count", count - 1);
                    m.tint.changeColor(Color.WHITE.cpy(), 0.6F);
                    break;

                case "BronzeAutomaton":
                    count = (int) ReflectionHacks.getPrivate(m, m.getClass(), "numTurns");
                    if (count <= 0)
                    {
                        count = 4;
                    }
                    ReflectionHacks.setPrivate(m, m.getClass(), "numTurns", count);
                    break;

                case "SlaverRed":
                    if (entangleReset)
                    {
                        ReflectionHacks.setPrivate(m, m.getClass(), "usedEntangle", false);
                        entangleReset = false;
                    }
                    break;

                case "Looter":
                case "Mugger":
                    m.tint.changeColor(Color.WHITE.cpy(), 0.6F);
                    break;

                // Champ has a bunch of hardcoded things since A19
                case "Champ":
                    ReflectionHacks.setPrivate(m, m.getClass(), "numTurns", champNumTurns);
                    ReflectionHacks.setPrivate(m, m.getClass(), "forgeTimes", forgeTimes);
                    ReflectionHacks.setPrivate(m, m.getClass(), "thresholdReached", champThresholdReached);
                    break;

                case "BookOfStabbing":
                    ReflectionHacks.setPrivate(m, m.getClass(), "stabCount", StabbyMcStabs);
                    break;

                case "TheCollector":
                    int turnsTaken = (int) ReflectionHacks.getPrivate(m, m.getClass(), "turnsTaken");
                    ReflectionHacks.setPrivate(m, m.getClass(), "turnsTaken", turnsTaken - 1);
                    break;

                case "Lagavulin":
                    switch (m.nextMove)
                    {
                        case 5: // sleep
                            int idleCount = (int) ReflectionHacks.getPrivate(m, m.getClass(), "idleCount");
                            ReflectionHacks.setPrivate(m, m.getClass(), "idleCount", idleCount - 1);
                            break;
                        case 1:
                            ReflectionHacks.setPrivate(m, m.getClass(), "debuffTurnCount", 2);
                            break;
                        case 3:
                            int debuffTurnCount = (int) ReflectionHacks.getPrivate(m, m.getClass(), "debuffTurnCount");
                            ReflectionHacks.setPrivate(m, m.getClass(), "debuffTurnCount", debuffTurnCount - 1);
                            break;
                    }
                    break;
                case "Chosen":
                    if (m.nextMove == 4)
                    {
                        ReflectionHacks.setPrivate(m, m.getClass(), "usedHex", false);
                    }
                    break;
                case "SphericGuardian":
                    if (m.nextMove == 4)
                    {
                        ReflectionHacks.setPrivate(m, m.getClass(), "secondMove", true);
                    }
                    break;
                case "Maw":
                    turnCount = (int) ReflectionHacks.getPrivate(m, m.getClass(), "turnCount");
                    ReflectionHacks.setPrivate(m, m.getClass(), "turnCount", turnCount - 1);
                    break;
                case "Hexaghost":
                    Hexaghost hexa = (Hexaghost) m;
                    int orbActiveCount = (int) ReflectionHacks.getPrivate(hexa, Hexaghost.class, "orbActiveCount");
                    if (orbActiveCount == 0)
                    {
                        ReflectionHacks.setPrivate(hexa, Hexaghost.class, "orbActiveCount", 6);
                    }
                    else
                    {
                        ReflectionHacks.setPrivate(hexa, Hexaghost.class, "orbActiveCount", orbActiveCount - 1);
                    }
                    break;

                case "CorruptHeart":
                    turnCount = (int) ReflectionHacks.getPrivate(m, m.getClass(), "moveCount");
                    ReflectionHacks.setPrivate(m, m.getClass(), "moveCount", turnCount - 1);
                    break;

                case "SpireShield":
                    turnCount = (int) ReflectionHacks.getPrivate(m, m.getClass(), "moveCount");
                    ReflectionHacks.setPrivate(m, m.getClass(), "moveCount", turnCount - 1);
                    break;
                case "SpireSpear":
                    turnCount = (int) ReflectionHacks.getPrivate(m, m.getClass(), "moveCount");
                    ReflectionHacks.setPrivate(m, m.getClass(), "moveCount", turnCount - 1);
                    break;
            }
            return true;
        }
    }
}

