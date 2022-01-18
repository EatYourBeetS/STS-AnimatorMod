package pinacolada.cards.pcl.series.DateALive;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.subscribers.OnShuffleSubscriber;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;

import java.util.ArrayList;

public class MukuroHoshimiya extends PCLCard implements StartupCard, OnShuffleSubscriber
{
    public static final PCLCardData DATA = Register(MukuroHoshimiya.class).SetAttack(2, CardRarity.RARE, PCLAttackType.Electric).SetSeriesFromClassPackage();
    private final ArrayList<AbstractCard> cardList = new ArrayList<>();


    public MukuroHoshimiya()
    {
        super(DATA);

        Initialize(14, 0, 4, 3);
        SetUpgrade(0,0,-1);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Light(1, 0, 2);
        SetAffinity_Silver(1, 0 ,0);
        SetAffinity_Dark(0,0,1);
    }

    @Override
    protected float GetInitialDamage()
    {
        return baseDamage + (player.drawPile.size() / (float) magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.PSYCHOKINESIS).forEach(d -> d.AddCallback(() ->
                PCLActions.Bottom.Scry(secondaryValue).AddCallback(cards ->
                        {
                            for (AbstractCard card : cards) {
                                if (!card.hasTag(DELAYED)) {
                                    cardList.add(card);
                                    card.tags.add(DELAYED);
                                }
                            }
                            if (cardList.size() > 0) {
                                PCLCombatStats.onShuffle.Subscribe(this);
                            }
                        }
                )).SetDuration(0.5f,false));

    }

    @Override
    public boolean atBattleStartPreDraw()
    {
        OnShuffle(false);

        return false;
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);
    }

    @Override
    public void OnShuffle(boolean triggerRelics)
    {
        PCLActions.Top.Callback(() -> {
            for (AbstractCard card : cardList) {
                if (card != null) {
                    card.tags.remove(DELAYED);
                }

            }
            cardList.clear();
            PCLCombatStats.onShuffle.Unsubscribe(this);
        });
    }
}