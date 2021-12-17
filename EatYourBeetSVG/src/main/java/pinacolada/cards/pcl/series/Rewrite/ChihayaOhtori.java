package pinacolada.cards.pcl.series.Rewrite;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.special.RefreshHandLayout;
import pinacolada.cards.base.*;
import pinacolada.cards.base.modifiers.CostModifiers;
import pinacolada.effects.AttackEffects;
import pinacolada.stances.EnduranceStance;
import pinacolada.utilities.PCLActions;

public class ChihayaOhtori extends PCLCard
{
    public static final PCLCardData DATA = Register(ChihayaOhtori.class).SetAttack(3, CardRarity.UNCOMMON, PCLAttackType.Normal).SetSeriesFromClassPackage();

    private static final CardEffectChoice choices = new CardEffectChoice();

    public ChihayaOhtori()
    {
        super(DATA);

        Initialize(12, 0, 3, 3);
        SetUpgrade(0, 0, -2);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Orange(1, 0, 2);
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        Refresh(null);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        RefreshCost();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_HEAVY);

        String[] text = DATA.Strings.EXTENDED_DESCRIPTION;

        if (choices.TryInitialize(this))
        {
            choices.AddEffect(text[0], this::Effect1);
            choices.AddEffect(text[1], this::Effect2);
        }

        choices.Select(1, m);
    }

    private void Effect1(AbstractCard card, AbstractPlayer p, AbstractMonster m)
    {
        PCLActions.Top.FetchFromPile(name, 1, player.discardPile)
        .SetOptions(false, false)
        .SetFilter(c -> (c instanceof PCLCard && ((PCLCard) c).affinities.GetLevel(PCLAffinity.Orange) > 0))
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                PCLActions.Bottom.Motivate(cards.get(0), 1);
                PCLActions.Bottom.Add(new RefreshHandLayout());
            }
        });
    }

    private void Effect2(AbstractCard card, AbstractPlayer p, AbstractMonster m)
    {
        PCLActions.Top.GainTemporaryArtifact(secondaryValue);
    }

    public void RefreshCost()
    {
        int orange = GetHandAffinity(PCLAffinity.Orange, false);
        if (EnduranceStance.IsActive()) {
            CostModifiers.For(this).Set(-1);
        }
        else {
            CostModifiers.For(this).Set(0);
        }
    }
}