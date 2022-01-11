package pinacolada.cards.pcl.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.VFX;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;
import java.util.List;

public class ThrowingKnife extends PCLCard
{
    public static final PCLCardData DATA = Register(ThrowingKnife.class)
            .SetAttack(0, CardRarity.SPECIAL, PCLAttackType.Ranged)
            .SetColor(CardColor.COLORLESS)
            .SetMultiformData(3, false, false, false, true);
    public static final int INDEX_WEAK = 0;
    public static final int INDEX_VULNERABLE = 1;
    public static final int INDEX_POISON = 2;

    protected Color color = Color.WHITE.cpy();

    public static List<ThrowingKnife> GetAllCards()
    {
        List<ThrowingKnife> result = new ArrayList<>();
        result.add(new ThrowingKnife(INDEX_WEAK, 0));
        result.add(new ThrowingKnife(INDEX_VULNERABLE, 0));
        result.add(new ThrowingKnife(INDEX_POISON, 0));
        return result;
    }

    public static ThrowingKnife GetRandomCard()
    {
        return new ThrowingKnife(rng.random(0, 2), 0);
    }

    public ThrowingKnife()
    {
        this(0, 0);
    }

    private ThrowingKnife(int form, int timesUpgraded)
    {
        super(DATA, form, timesUpgraded);

        Initialize(2, 0, 1, 2);
        SetUpgrade(2, 0);

        SetPurge(true);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        this.cardText.OverrideDescription(PCLJUtils.Format(rawDescription, cardData.Strings.EXTENDED_DESCRIPTION[form]), true);

        switch (form) {
            case INDEX_WEAK:
                this.color = new Color(0.4f, 0.6f, 0.4f, 1f);
            case INDEX_VULNERABLE:
                this.color = new Color(0.8f, 0.2f, 0.2f, 1f);
            case INDEX_POISON:
                this.color = new Color(0.2f, 1.0f, 0.2f, 1f);
        }

        return super.SetForm(form, timesUpgraded);
    };

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        PCLActions.Bottom.Callback(() ->
        {
            player.discardPile.removeCard(this);
            freeToPlayOnce = true;
            purgeOnUse = true;
            PCLGameUtilities.PlayManually(this, null);
        });
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        if (m != null && misc == INDEX_WEAK)
        {
            PCLGameUtilities.GetPCLIntent(m).AddWeak();
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (m == null || PCLGameUtilities.IsDeadOrEscaped(m))
        {
            PCLActions.Bottom.DealCardDamageToRandomEnemy(this, AttackEffects.NONE).forEach(d -> d
            .SetDamageEffect(this::OnDamage));
        }
        else
        {
            PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.NONE).forEach(d -> d
            .SetDamageEffect(this::OnDamage));
        }
    }

    protected float OnDamage(AbstractCreature target)
    {
        final AbstractMonster m = PCLJUtils.SafeCast(target, AbstractMonster.class);
        if (m == null || PCLGameUtilities.IsDeadOrEscaped(m))
        {
            return 0;
        }

        PCLPowerHelper ph = PCLPowerHelper.Poison;
        switch (auxiliaryData.form) {
            case INDEX_WEAK:
                ph = PCLPowerHelper.Weak;
                break;
            case INDEX_VULNERABLE:
                ph = PCLPowerHelper.Vulnerable;
                break;
        }

        PCLActions.Top.StackPower(TargetHelper.Normal(m), ph, magicNumber);

        return PCLGameEffects.List.Add(VFX.ThrowDagger(m.hb, 0.1f).SetColor(color)).duration * 0.33f;
    }
}