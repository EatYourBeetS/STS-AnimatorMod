package pinacolada.cards.pcl.series.Fate;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.RandomizedList;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.tokens.AffinityToken;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class EmiyaKiritsugu extends PCLCard
{
    public static final PCLCardData DATA = Register(EmiyaKiritsugu.class)
            .SetAttack(1, CardRarity.RARE, PCLAttackType.Ranged)
            .SetMultiformData(2, false)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                data.AddPreview(AffinityToken.GetCard(PCLAffinity.Light), false);
                data.AddPreview(AffinityToken.GetCard(PCLAffinity.Dark), false);
            });

    public EmiyaKiritsugu()
    {
        super(DATA);

        Initialize(9, 7, 3, 3);
        SetUpgrade(2, 2);

        SetAffinity_Light(1, 0, 0);
        SetAffinity_Dark(1, 0, 0);
        SetAffinity_Blue(0, 0, 1);
        SetAffinity_Orange(1,0,3);

        SetExhaust(true);
        SetRetainOnce(true);
    }

    @Override
    protected String GetRawDescription(Object... args)
    {
        return super.GetRawDescription(cardData.Strings.EXTENDED_DESCRIPTION[auxiliaryData.form]);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        SetUnplayable(PCLJUtils.Count(player.drawPile.group, c -> c.rarity == CardRarity.UNCOMMON) < magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        final RandomizedList<AbstractCard> uncommonCards = new RandomizedList<>();
        uncommonCards.AddAll(PCLJUtils.Filter(p.drawPile.group, c -> c.rarity == CardRarity.UNCOMMON));
        if (upgraded && auxiliaryData.form == 1) {
            uncommonCards.AddAll(PCLJUtils.Filter(p.discardPile.group, c -> c.rarity == CardRarity.UNCOMMON));
            uncommonCards.AddAll(PCLJUtils.Filter(p.hand.group, c -> c.rarity == CardRarity.UNCOMMON));
        }
        final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        while (group.size() < magicNumber && uncommonCards.Size() > 0) {
            group.addToBottom(uncommonCards.Retrieve(rng, true));
        }
        if (group.size() < magicNumber)
        {
            return;
        }

        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.GUNSHOT).forEach(d -> d
        .SetSoundPitch(0.6f, 0.7f)
        .SetVFXColor(Color.GOLD));

        PCLActions.Bottom.ExhaustFromPile(name, 1, group)
        .SetOptions(false, false)
        .AddCallback(m, (enemy, cards) ->
        {
            for (AbstractCard c : cards)
            {
                PCLCardAffinities a = PCLGameUtilities.GetPCLAffinities(c);
                if (a != null)
                {
                    if (a.GetLevel(PCLAffinity.Light, true) > 0)
                    {
                        PCLActions.Bottom.MakeCardInHand(AffinityToken.GetCopy(PCLAffinity.Dark, false));
                    }
                    if (a.GetLevel(PCLAffinity.Dark, true) > 0)
                    {
                        PCLActions.Bottom.MakeCardInHand(AffinityToken.GetCopy(PCLAffinity.Light, false));
                    }
                    if (a.GetLevel(PCLAffinity.Silver, true) > 0)
                    {
                        PCLActions.Bottom.GainTechnic(secondaryValue);
                    }
                }
            }
        });
    }
}