package eatyourbeets.cards.animator.colorless.rare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FadingPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.CollectorCurseEffect;
import eatyourbeets.actions.utility.WaitRealtimeAction;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.SFX;
import eatyourbeets.monsters.Bosses.TheUnnamed;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.replacement.GenericFadingPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class Kira extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Kira.class)
            .SetSkill(1, CardRarity.RARE)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.DeathNote);

    private AbstractMonster lastTargetEnemy = null;
    private AbstractMonster targetEnemy = null;

    public Kira()
    {
        super(DATA);

        Initialize(0, 0, 0, 3);
        SetUpgrade(0, 0, 0, -1);

        SetAffinity_Blue(1);
        SetAffinity_Orange(2);
        SetAffinity_Dark(2);

        SetExhaust(true);
        SetEthereal(true);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        if (m != null)
        {
            GameUtilities.GetIntent(m).AddStrength(secondaryValue);
        }
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m)
    {
        return super.canUse(p, m) && !(m instanceof TheUnnamed);
    }

    @Override
    public void calculateCardDamage(AbstractMonster m)
    {
        super.calculateCardDamage(m);

        targetDrawScale = 1f;
        target_x = Settings.WIDTH * 0.4f;
        target_y = Settings.HEIGHT * 0.4f;
        targetEnemy = m;
    }

    @Override
    public void update()
    {
        super.update();

        if (lastTargetEnemy != targetEnemy)
        {
            UpdateCurrentEffect(targetEnemy);
            lastTargetEnemy = targetEnemy;
        }

        targetEnemy = null;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(p, new StrengthPower(m, secondaryValue));

        if (m.type == AbstractMonster.EnemyType.BOSS)
        {
            CardCrawlGame.music.silenceBGMInstantly();
            CardCrawlGame.music.silenceTempBgmInstantly();

            GameActions.Bottom.SFX(SFX.ANIMATOR_KIRA_POWER);
            GameEffects.Queue.Callback(new WaitRealtimeAction(9f), CardCrawlGame.music::unsilenceBGM);
        }
        else
        {
            GameActions.Bottom.SFX(SFX.MONSTER_COLLECTOR_DEBUFF);
        }

        GameActions.Bottom.VFX(new CollectorCurseEffect(m.hb.cX, m.hb.cY), 2f);

        final int countdown = GetCountdown(m);
        if (countdown <= 0)
        {
            return;
        }

        AbstractPower fading = m.getPower(FadingPower.POWER_ID);
        if (fading != null)
        {
            fading.amount = countdown;
        }
        else
        {
            fading = m.getPower(GenericFadingPower.POWER_ID);
            if (fading != null)
            {
                fading.amount = countdown;
            }
            else
            {
                m.powers.add(new GenericFadingPower(m, countdown));
            }
        }

        if (info.TryActivateLimited())
        {
            player.increaseMaxHp(GetMaxHpBonus(countdown), true);
        }
    }

    private void UpdateCurrentEffect(AbstractMonster monster)
    {
        GameUtilities.ModifyMagicNumber(this, GetCountdown(monster), false);

        if (magicNumber > 0)
        {
            String text = cardData.Strings.EXTENDED_DESCRIPTION[1];
            if (CombatStats.CanActivateLimited(cardID))
            {
                text = JUtils.Format(cardData.Strings.EXTENDED_DESCRIPTION[0], GetMaxHpBonus(magicNumber)) + text;
            }
            cardText.OverrideDescription(text, true);
        }
        else
        {
            cardText.OverrideDescription(null, true);
        }
    }

    private int GetCountdown(AbstractMonster m)
    {
        if (m == null)
        {
            return 0;
        }
        else if (m instanceof TheUnnamed)
        {
            ((TheUnnamed) m).TriedUsingDeathNote();
            return 0;
        }
        else if (m.currentHealth <= 20)
        {
            return 1;
        }
        else if (m.currentHealth <= 33)
        {
            return 2;
        }
        else if (m.currentHealth <= 50)
        {
            return 3;
        }
        else if (m.currentHealth <= 100)
        {
            return 4;
        }
        else if (m.currentHealth <= 180)
        {
            return 5;
        }
        else if (m.currentHealth <= 280)
        {
            return 6;
        }
        else if (m.currentHealth <= 500)
        {
            return 7;
        }

        return 7 + (m.currentHealth / 500);
    }

    protected int GetMaxHpBonus(int turns)
    {
        return turns == 1 ? 1 : (turns / 2);
    }
}