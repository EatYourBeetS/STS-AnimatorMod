package eatyourbeets.cards.animator.colorless.rare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FadingPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.CollectorCurseEffect;
import eatyourbeets.actions.utility.WaitRealtimeAction;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.SFX;
import eatyourbeets.monsters.Bosses.TheUnnamed;
import eatyourbeets.powers.replacement.GenericFadingPower;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.JUtils;

public class Kira extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Kira.class)
            .SetSkill(1, CardRarity.RARE)
            .SetMaxCopies(1)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.DeathNote);
    public static final String[] DESCRIPTIONS = DATA.Strings.EXTENDED_DESCRIPTION;

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
    public void OnDrag(AbstractMonster m)
    {
        if (m != null)
        {
            if (magicNumber > 0)
            {
                GR.UI.AddPostRender(sb ->
                {
                    final String message = JUtils.Format(cardData.Strings.EXTENDED_DESCRIPTION[0], magicNumber);
                    FontHelper.renderDeckViewTip(sb, message, 96f * Settings.scale, Settings.CREAM_COLOR);
                });
            }

            targetEnemy = m;
        }
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m)
    {
        return super.canUse(p, m) && !(m instanceof TheUnnamed);
    }

    @Override
    public void update()
    {
        super.update();

        if (lastTargetEnemy != targetEnemy)
        {
            UpdateCountdown(targetEnemy);
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
            fading.amount = magicNumber;
        }
        else
        {
            fading = m.getPower(GenericFadingPower.POWER_ID);
            if (fading != null)
            {
                fading.amount = magicNumber;
            }
            else
            {
                m.powers.add(new GenericFadingPower(m, magicNumber));
            }
        }
    }

    private void UpdateCountdown(AbstractMonster m)
    {
        if (m == null)
        {
            magicNumber = 0;
        }
        else if (m instanceof TheUnnamed)
        {
            magicNumber = 0;
            ((TheUnnamed) m).TriedUsingDeathNote();
        }
        else if (m.currentHealth <= 20)
        {
            magicNumber = 1;
        }
        else if (m.currentHealth <= 33)
        {
            magicNumber = 2;
        }
        else if (m.currentHealth <= 50)
        {
            magicNumber = 3;
        }
        else if (m.currentHealth <= 100)
        {
            magicNumber = 4;
        }
        else if (m.currentHealth <= 180)
        {
            magicNumber = 5;
        }
        else if (m.currentHealth <= 280)
        {
            magicNumber = 6;
        }
        else if (m.currentHealth <= 500)
        {
            magicNumber = 7;
        }
        else
        {
            magicNumber = 8;
        }
    }
}