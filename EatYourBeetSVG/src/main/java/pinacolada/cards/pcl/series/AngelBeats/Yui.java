package pinacolada.cards.pcl.series.AngelBeats;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.cards.pcl.special.GirlDeMo;
import pinacolada.cards.pcl.tokens.AffinityToken;
import pinacolada.interfaces.subscribers.OnAfterlifeSubscriber;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;

public class Yui extends PCLCard implements OnAfterlifeSubscriber
{
    public static final PCLCardData DATA = Register(Yui.class).SetSkill(2, CardRarity.UNCOMMON, eatyourbeets.cards.base.EYBCardTarget.None, true).SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new GirlDeMo(), false));

    public Yui()
    {
        super(DATA);

        Initialize(0, 0, 2, 2);
        SetAffinity_Star(1, 0, 0);
        SetExhaust(true);
        SetAfterlife(true);

        SetAffinityRequirement(PCLAffinity.General, 12);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);
        PCLCombatStats.onAfterlife.Subscribe(this);
    }

    @Override
    protected void OnUpgrade()
    {
        SetAffinityRequirement(PCLAffinity.General, 6);
    }

    @Override
    public AbstractAttribute GetSecondaryInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainTemporaryHP(magicNumber);
        PCLActions.Bottom.MakeCardInHand(AffinityToken.GetCard(PCLAffinity.Light)).SetUpgrade(upgraded, false).AddCallback(
                () -> {
                    PCLActions.Bottom.Motivate(secondaryValue);
                }
        );
    }

    @Override
    public void OnAfterlife(AbstractCard playedCard, AbstractCard fuelCard) {
        if (playedCard == this && CheckAffinity(PCLAffinity.General))
        {
            PCLActions.Bottom.MakeCardInDrawPile(new GirlDeMo());
        }
    }
}