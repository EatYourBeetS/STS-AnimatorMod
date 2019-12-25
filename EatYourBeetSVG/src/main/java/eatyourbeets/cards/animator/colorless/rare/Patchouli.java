package eatyourbeets.cards.animator.colorless.rare;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.vfx.combat.*;
import eatyourbeets.actions.damage.DealDamageToRandomEnemy;
import eatyourbeets.cards.animator.special.OrbCore;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.csharp.Action;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;
import java.util.HashSet;

public class Patchouli extends AnimatorCard implements Spellcaster, StartupCard
{
    private int cachedOrbAmount;

    public static final String ID = Register(Patchouli.class.getSimpleName(), EYBCardBadge.Special);

    public Patchouli()
    {
        super(ID, 3, CardType.ATTACK, CardColor.COLORLESS, CardRarity.RARE, CardTarget.SELF);

        Initialize(11, 0, 0, 2);
        SetUpgrade(3, 0, 0, 0);

        SetSynergy(Synergies.TouhouProject);
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        ArrayList<AbstractOrb> orbs = AbstractDungeon.actionManager.orbsChanneledThisCombat;
        if (cachedOrbAmount != orbs.size())
        {
            HashSet<String> uniqueOrbs = new HashSet<>();
            for (AbstractOrb orb : orbs)
            {
                uniqueOrbs.add(orb.ID);
            }

            magicNumber = uniqueOrbs.size();
            isMagicNumberModified = (magicNumber != baseMagicNumber);
            cachedOrbAmount = orbs.size();
        }
    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp)
    {
        return super.calculateModifiedCardDamage(player, mo, tmp + Spellcaster.GetScaling());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (magicNumber > 0)
        {
            RandomizedList<Action> actions = new RandomizedList<>();

            for (int i = 0; i < magicNumber; i++)
            {
                if (actions.Count() == 0)
                {
                    actions.Add(this::Aether);
                    actions.Add(this::Fire);
                    actions.Add(this::Frost);
                    actions.Add(this::Lightning);
                }

                actions.Retrieve(AbstractDungeon.cardRandomRng).Invoke();
                GameActions.Bottom.WaitRealtime(0.2f);
            }
        }
    }

    private void Lightning()
    {
        CreateDamageAction().SetDamageEffect(e ->
        {
            CardCrawlGame.sound.play("ORB_LIGHTNING_EVOKE", 0.2f);
            GameEffects.Queue.Add(new LightningEffect(e.drawX, e.drawY));
        });
    }

    private void Frost()
    {
        CreateDamageAction().SetDamageEffect(e ->
        {
            MonsterGroup monsters = AbstractDungeon.getMonsters();
            int frostCount = monsters.monsters.size() + 5;

            CardCrawlGame.sound.playA("ORB_FROST_CHANNEL", -0.25F - (float) frostCount / 200.0F);
            for (int f = 0; f < frostCount; f++)
            {
                GameEffects.Queue.Add(new FallingIceEffect(frostCount, monsters.shouldFlipVfx()));
            }
        });
    }

    private void Aether()
    {
        CreateDamageAction().SetDamageEffect(__ ->
        {
            CardCrawlGame.sound.play("ATTACK_WHIRLWIND", 0.2f);
            GameEffects.Queue.Add(new WhirlwindEffect());
        });
    }

    private void Fire()
    {
        CreateDamageAction().SetDamageEffect(e ->
        {
            CardCrawlGame.sound.play("ATTACK_FIRE", 0.2f);
            GameEffects.Queue.Add(new FireballEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, e.hb.cX, e.hb.cY));
        });
    }

    private DealDamageToRandomEnemy CreateDamageAction()
    {
        return GameActions.Bottom.DealDamageToRandomEnemy(baseDamage + Spellcaster.GetScaling(),
        damageTypeForTurn, AbstractGameAction.AttackEffect.NONE).SetOptions(true, false);
    }

    @Override
    public boolean atBattleStartPreDraw()
    {
        if (EffectHistory.TryActivateLimited(cardID))
        {
            GameActions.Bottom.Wait(0.3f);
            GameActions.Bottom.MakeCardInDiscardPile(JavaUtilities.GetRandomElement(OrbCore.GetAllCores()).makeCopy());

            return true;
        }

        return false;
    }
}