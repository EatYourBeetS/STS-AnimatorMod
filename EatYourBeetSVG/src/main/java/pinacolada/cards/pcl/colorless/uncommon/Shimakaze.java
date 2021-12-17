package pinacolada.cards.pcl.colorless.uncommon;

import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

public class Shimakaze extends PCLCard
{
    public static final PCLCardData DATA = Register(Shimakaze.class)
            .SetAttack(1, CardRarity.UNCOMMON).SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.Kancolle);

    public Shimakaze()
    {
        super(DATA);

        Initialize(2, 2, 3, 2);
        
        SetAffinity_Green(1);
        SetAffinity_Silver(1);
        SetAffinity_Star(0,0,1);
        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_LIGHT);

        PCLActions.Bottom.AddAffinity(PCLJUtils.Random(PCLAffinity.Extended()), secondaryValue);
        PCLActions.Bottom.Draw(magicNumber);
        PCLActions.Bottom.MakeCardInDrawPile(new Dazed());
    }
}