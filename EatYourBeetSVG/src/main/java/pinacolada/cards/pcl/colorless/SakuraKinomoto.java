package pinacolada.cards.pcl.colorless;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.*;
import pinacolada.effects.SFX;
import pinacolada.effects.VFX;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class SakuraKinomoto extends PCLCard
{
    public static final PCLCardData DATA = Register(SakuraKinomoto.class)
            .SetAttack(3, CardRarity.RARE, PCLAttackType.Elemental)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.CardcaptorSakura);

    public SakuraKinomoto()
    {
        super(DATA);

        Initialize(4, 0, 0, 3);
        SetUpgrade(2, 0, 0, 1);

        SetAffinity_Blue(1, 0, 4);

        SetExhaust(true);
    }

    @Override
    protected float GetInitialDamage()
    {
        return super.GetInitialDamage() + player.masterDeck.size();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.SFX(SFX.ORB_LIGHTNING_PASSIVE, 1.35f, 1.45f);
        PCLActions.Bottom.DealCardDamage(this, m, AbstractGameAction.AttackEffect.NONE).forEach(d -> d
        .SetDamageEffect(e -> PCLGameEffects.Queue.Add(VFX.SmallLaser(player.hb, e.hb, Color.PINK)).duration * 0.4f)
        .AddCallback(enemy ->
        {
            if (PCLGameUtilities.InEliteOrBossRoom() && PCLGameUtilities.IsFatal(enemy, false) && CombatStats.CanActivateLimited(cardID))
            {
                final CardGroup choices = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                for (int i = 0; i < secondaryValue; i++)
                {
                    final AbstractCard card = GR.PCL.Dungeon.GetRandomRewardCard(choices.group, false, true);
                    if (card != null)
                    {
                        choices.group.add(card);
                    }
                }

                PCLActions.Top.SelectFromPile(name, 1, choices)
                .SetMessage(GR.PCL.Strings.GridSelection.ChooseOneCard)
                .SetOptions(false, true)
                .AddCallback(cards ->
                {
                    if (cards.size() > 0 && info.TryActivateLimited())
                    {
                        PCLGameEffects.TopLevelList.ShowAndObtain(cards.get(0));
                    }
                })
                .IsCancellable(false);
                PCLActions.Top.WaitRealtime(0.5f);
            }
        }));
    }
}