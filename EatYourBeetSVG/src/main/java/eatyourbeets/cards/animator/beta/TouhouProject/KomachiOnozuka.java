package eatyourbeets.cards.animator.beta.TouhouProject;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.relics.animator.beta.ShinigamisFerry;
import eatyourbeets.relics.animator.unnamedReign.AncientMedallion;
import eatyourbeets.relics.animator.unnamedReign.UnnamedReignRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class KomachiOnozuka extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KomachiOnozuka.class).SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Piercing);

    private static final AbstractRelic relicReward = new ShinigamisFerry();

    public KomachiOnozuka()
    {
        super(DATA);

        Initialize(10, 0, 2, 0);
        SetUpgrade(2, 0, 1, 0);
        SetScaling(0, 0, 1);

        SetSynergy(Synergies.TouhouProject);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY)
        .AddCallback(enemy ->
        {
            AbstractRoom room = AbstractDungeon.getCurrRoom();
            if ((room instanceof MonsterRoomElite || room instanceof MonsterRoomBoss)
                && GameUtilities.TriggerOnKill(enemy, false)
                && CombatStats.TryActivateLimited(cardID))
            {
                ObtainReward();
            }
        });
        GameActions.Bottom.ApplyVulnerable(p, m, magicNumber);
    }

    public void ObtainReward()
    {
        if (UnnamedReignRelic.IsEquipped())
        {
            GameUtilities.GetCurrentRoom(true).addRelicToRewards(new AncientMedallion());
        }
        else
        {
            GameUtilities.GetCurrentRoom(true).addRelicToRewards(relicReward.makeCopy());
        }
    }
}

