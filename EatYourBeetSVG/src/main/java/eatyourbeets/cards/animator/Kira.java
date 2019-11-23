package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FadingPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.CollectorCurseEffect;
import eatyourbeets.powers.common.GenericFadingPower;
import eatyourbeets.resources.Resources_Animator;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.monsters.Bosses.TheUnnamed;
import eatyourbeets.utilities.JavaUtilities;

public class Kira extends AnimatorCard
{
    public static final String ID = Register(Kira.class.getSimpleName());
    public static final String[] DESCRIPTIONS = Resources_Animator.GetCardStrings(ID).EXTENDED_DESCRIPTION;

    private int countdown;
    private AbstractMonster lastTargetEnemy = null;
    private AbstractMonster targetEnemy = null;

    public Kira()
    {
        super(ID, 1, CardType.SKILL, CardColor.COLORLESS, CardRarity.RARE, CardTarget.SELF_AND_ENEMY);

        Initialize(0, 0, 3);

        AddExtendedDescription();

        SetExhaust(true);
        SetEthereal(true);
        SetSynergy(Synergies.DeathNote);
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
            updateCurrentEffect(targetEnemy);

            lastTargetEnemy = targetEnemy;
        }

        targetEnemy = null;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.ApplyPower(p, m, new StrengthPower(m, this.secondaryValue), this.secondaryValue);
        updateCountdown(m);

        GameActionsHelper.AddToBottom(new SFXAction("MONSTER_COLLECTOR_DEBUFF"));
        GameActionsHelper.AddToBottom(new VFXAction(new CollectorCurseEffect(m.hb.cX, m.hb.cY), 2.0F));

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

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(-1);
        }
    }

    private void updateCurrentEffect(AbstractMonster monster)
    {
        if (monster == null)
        {
            cardText.OverrideDescription(null, true);
        }
        else if (monster instanceof TheUnnamed)
        {
            cardText.OverrideDescription(null, true);
            ((TheUnnamed)monster).TriedUsingDeathNote();
        }
        else
        {
            updateCountdown(monster);
            cardText.OverrideDescription(JavaUtilities.Format(cardData.strings.EXTENDED_DESCRIPTION[2], countdown), true);
        }
    }

    private void updateCountdown(AbstractMonster m)
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