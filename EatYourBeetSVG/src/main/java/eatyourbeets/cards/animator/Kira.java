package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FadingPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.vfx.CollectorCurseEffect;
import eatyourbeets.AnimatorResources;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Kira extends AnimatorCard
{
    public static final String ID = CreateFullID(Kira.class.getSimpleName());
    public static final String[] DESCRIPTIONS = AnimatorResources.GetCardStrings(ID).EXTENDED_DESCRIPTION;

    private int countdown;
    private AbstractMonster lastTarget = null;
    private AbstractMonster target = null;

    public Kira()
    {
        super(ID, 1, CardType.SKILL, CardColor.COLORLESS, CardRarity.RARE, CardTarget.SELF_AND_ENEMY);

        Initialize(0, 0);

        this.baseSecondaryValue = this.secondaryValue = 3;
        this.isEthereal = true;
        this.exhaust = true;

        AddExtendedDescription();

        SetSynergy(Synergies.DeathNote);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo)
    {
        super.calculateCardDamage(mo);

        target = mo;
    }

    @Override
    public void update()
    {
        super.update();

        if (lastTarget != target)
        {
            updateCurrentEffect(target);

            lastTarget = target;
        }

        target = null;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.ApplyPower(p, p, new VulnerablePower(p, this.secondaryValue, false), this.secondaryValue);
        updateCountdown(m);

        GameActionsHelper.AddToBottom(new SFXAction("MONSTER_COLLECTOR_DEBUFF"));
        GameActionsHelper.AddToBottom(new VFXAction(new CollectorCurseEffect(m.hb.cX, m.hb.cY), 2.0F));
        FadingPower fading = (FadingPower) m.getPower(FadingPower.POWER_ID);

        if (fading != null)
        {
            fading.amount = countdown;
        }
        else
        {
            m.powers.add(new FadingPower(m, countdown));
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeSecondaryValue(-1);
        }
    }

    private void updateCurrentEffect(AbstractMonster monster)
    {
        if (monster == null)
        {
            rawDescription = cardStrings.DESCRIPTION;
        }
        else
        {
            updateCountdown(monster);
            rawDescription = cardStrings.EXTENDED_DESCRIPTION[2].replace("#", String.valueOf(countdown));
        }

        initializeDescription();
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