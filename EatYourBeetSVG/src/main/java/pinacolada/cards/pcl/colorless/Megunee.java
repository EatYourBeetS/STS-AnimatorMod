package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.cards.pcl.special.Megunee_Zombie;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;

public class Megunee extends PCLCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final PCLCardData DATA = Register(Megunee.class).SetSkill(1, CardRarity.RARE, eatyourbeets.cards.base.EYBCardTarget.None).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.GakkouGurashi)
            .PostInitialize(data -> data.AddPreview(new Megunee_Zombie(), false));

    private int turns;

    public Megunee()
    {
        super(DATA);

        Initialize(0, 7, 4, 10);
        SetUpgrade(0, 2, 1, 3);

        SetAffinity_Light(1, 0, 1);
        SetAffinity_Orange(1, 0, 0);

        SetSoul(2, 0, Megunee_Zombie::new);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.GainTemporaryHP(magicNumber);

        cooldown.ProgressCooldownAndTrigger(m);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        turns = rng.random(0, 3);
        PCLCombatStats.onStartOfTurnPostDraw.Subscribe(this);
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        if (player.exhaustPile.contains(this))
        {
            if (turns <= 0)
            {
                PCLActions.Bottom.GainTemporaryHP(secondaryValue);
                PCLCombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
            }
            else
            {
                turns -= 1;
            }
        }
        else
        {
            PCLCombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
        }
    }
}