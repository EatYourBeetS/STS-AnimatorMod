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
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.effects.SFX;
import eatyourbeets.monsters.Bosses.TheUnnamed;
import eatyourbeets.powers.replacement.GenericFadingPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Kira extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Kira.class)
            .SetSkill(1, CardRarity.RARE)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.DeathNote);
    public static final String[] DESCRIPTIONS = DATA.Strings.EXTENDED_DESCRIPTION;

    private int countdown;
    private AbstractMonster lastTargetEnemy = null;
    private AbstractMonster targetEnemy = null;

    public Kira()
    {
        super(DATA);

        Initialize(0, 0, 0, 2);
        SetUpgrade(0, 0, 0, -1);

        SetAffinity_Blue(2);
        SetAffinity_Dark(2);

        SetExhaust(true);
        SetEthereal(true);
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m)
    {
        return !(m instanceof TheUnnamed) && super.canUse(p, m);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo)
    {
        super.calculateCardDamage(mo);

        targetDrawScale = 1f;
        target_x = Settings.WIDTH * 0.4f;
        target_y = Settings.HEIGHT * 0.4f;

        targetEnemy = mo;
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
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.StackPower(p, new StrengthPower(m, secondaryValue));

        UpdateCountdown(m);

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
    }

    private void UpdateCurrentEffect(AbstractMonster monster)
    {
        if (monster == null)
        {
            cardText.OverrideDescription(null, true);
        }
        else if (monster instanceof TheUnnamed)
        {
            cardText.OverrideDescription(null, true);
            ((TheUnnamed) monster).TriedUsingDeathNote();
        }
        else
        {
            UpdateCountdown(monster);
            GameUtilities.ModifyMagicNumber(this, countdown, true);
            cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[0], true);
        }
    }

    private void UpdateCountdown(AbstractMonster m)
    {
        if (m == null)
        {
            countdown = 99;
        }
        else if (m.currentHealth <= 20)
        {
            countdown = 1;
        }
        else if (m.currentHealth <= 33)
        {
            countdown = 2;
        }
        else if (m.currentHealth <= 50)
        {
            countdown = 3;
        }
        else if (m.currentHealth <= 100)
        {
            countdown = 4;
        }
        else if (m.currentHealth <= 180)
        {
            countdown = 5;
        }
        else if (m.currentHealth <= 280)
        {
            countdown = 6;
        }
        else if (m.currentHealth <= 500)
        {
            countdown = 7;
        }
        else
        {
            countdown = 8;
        }
    }
}